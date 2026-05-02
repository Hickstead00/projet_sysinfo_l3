import { Injectable } from '@angular/core';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Maquette } from '../model/maquette';
import { Parametres } from '../model/parametres';

@Injectable({ providedIn: 'root' })
export class PdfExportService {
  private readonly BLEU_FONCE = '#002d5b';
  private readonly BLEU_CLAIR = '#edf2f8';
  private readonly GRIS_CLAIR = '#f8fafc';
  private readonly GRIS_TEXTE = '#475569';
  private readonly NOIR = '#1e2a3a';

  // Point d'entrée : génère le PDF complet de la maquette.
  // La variable `y` suit la position verticale (en mm) sur la page courante.
  // Chaque section retourne la nouvelle position `y` après dessin ;
  // un saut de page est déclenché si `y` dépasse le seuil bas de la page A4.
  exporterMaquette(maquette: Maquette, parametres?: Parametres): void {
    const doc = new jsPDF('p', 'mm', 'a4');
    let y = 15;

    y = this.dessinerEnTete(doc, maquette, y);

    const semestres = [...maquette.semestres].sort(
      (a, b) => a.numeroSemestre - b.numeroSemestre,
    );

    for (const semestre of semestres) {
      if (y > 240) {
        doc.addPage();
        y = 15;
      }

      y = this.dessinerSemestre(doc, semestre, y);
    }

    if (y > 220) {
      doc.addPage();
      y = 15;
    }

    y = this.dessinerRecapitulatif(doc, maquette, parametres, y);

    // Légende en bas de page si certaines UE apparaissent dans plusieurs maquettes
    const aDesUePartagees = maquette.semestres
      .flatMap((s) => s.ues)
      .some((ue) => ue.nbMaquettes > 1);

    if (aDesUePartagees) {
      if (y > 275) {
        doc.addPage();
        y = 15;
      }
      doc.setFontSize(8);
      doc.setFont('helvetica', 'bolditalic');
      doc.setTextColor(this.GRIS_TEXTE);
      doc.text('Les UE en gras sont partagées avec d\'autres maquettes.', 15, y + 3);
    }

    this.dessinerPiedDePage(doc);

    const nomFichier = maquette.nomMaquette.replace(/[^a-zA-Z0-9À-ÿ\s-]/g, '').replace(/\s+/g, '_');
    doc.save(`${nomFichier}.pdf`);
  }

  // Bandeau bleu foncé en haut de page avec nom, type, nb ECTS et date de génération
  private dessinerEnTete(doc: jsPDF, maquette: Maquette, y: number): number {
    doc.setFillColor(this.BLEU_FONCE);
    doc.rect(0, 0, 210, 35, 'F');

    doc.setTextColor('#ffffff');
    doc.setFontSize(18);
    doc.setFont('helvetica', 'bold');
    doc.text(maquette.nomMaquette, 15, 16);

    doc.setFontSize(10);
    doc.setFont('helvetica', 'normal');
    const type = maquette.typeMaquette === 'LICENCE' ? 'Licence' : 'Master';
    const nbSemestres = maquette.typeMaquette === 'LICENCE' ? 6 : 4;
    doc.text(`${type} — ${nbSemestres} semestres — ${maquette.ectsTotal} ECTS`, 15, 24);

    const date = new Date().toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    });
    doc.setFontSize(9);
    doc.text(`Généré le ${date}`, 15, 31);

    return 45;
  }

  // Tableau d'un semestre : en-tête bleu avec numéro + ECTS/volume,
  // puis une ligne par UE (nom, référent, CM, TD, TP, ECTS) et une ligne de totaux.
  // Les UE partagées avec d'autres maquettes sont mises en gras via didParseCell.
  private dessinerSemestre(
    doc: jsPDF,
    semestre: { numeroSemestre: number; ues: any[]; ectsTotal: number; volumeHoraireCm: number; volumeHoraireTd: number; volumeHoraireTp: number; volumeHoraireTotal: number },
    y: number,
  ): number {
    doc.setFillColor(this.BLEU_FONCE);
    doc.roundedRect(15, y, 180, 8, 1, 1, 'F');
    doc.setTextColor('#ffffff');
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.text(`Semestre ${semestre.numeroSemestre}`, 19, y + 5.5);

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.text(
      `${semestre.ectsTotal} ECTS — ${semestre.volumeHoraireTotal}h`,
      195,
      y + 5.5,
      { align: 'right' },
    );

    y += 10;

    const donnees = semestre.ues.map((ue) => {
      const referent =
        ue.referents && ue.referents.length > 0
          ? ue.referents.map((r: any) => `${r.prenom} ${r.nom}`).join(', ')
          : '—';
      return [ue.nomUe, referent, `${ue.cm}h`, `${ue.td}h`, `${ue.tp}h`, `${ue.ects}`];
    });

    // Set des index de lignes dont l'UE est partagée (nbMaquettes > 1),
    // utilisé dans didParseCell pour appliquer le gras sur ces lignes
    const uesPartagees = new Set(
      semestre.ues.filter((ue) => ue.nbMaquettes > 1).map((_ue, i) => i),
    );

    const totalCm = semestre.ues.reduce((s, ue) => s + ue.cm, 0);
    const totalTd = semestre.ues.reduce((s, ue) => s + ue.td, 0);
    const totalTp = semestre.ues.reduce((s, ue) => s + ue.tp, 0);

    donnees.push(['Total', '', `${totalCm}h`, `${totalTd}h`, `${totalTp}h`, `${semestre.ectsTotal}`]);

    autoTable(doc, {
      startY: y,
      margin: { left: 15, right: 15 },
      head: [['UE', 'Référent', 'CM', 'TD', 'TP', 'ECTS']],
      body: donnees,
      theme: 'grid',
      styles: {
        fontSize: 9,
        cellPadding: 3,
        textColor: this.NOIR,
        lineColor: '#e2e8f0',
        lineWidth: 0.3,
      },
      headStyles: {
        fillColor: this.BLEU_CLAIR,
        textColor: this.BLEU_FONCE,
        fontStyle: 'bold',
        fontSize: 9,
      },
      alternateRowStyles: {
        fillColor: this.GRIS_CLAIR,
      },
      columnStyles: {
        0: { cellWidth: 55 },
        1: { cellWidth: 50 },
        2: { cellWidth: 18, halign: 'center' },
        3: { cellWidth: 18, halign: 'center' },
        4: { cellWidth: 18, halign: 'center' },
        5: { cellWidth: 18, halign: 'center' },
      },
      // Callback appelé par jspdf-autotable pour chaque cellule avant le rendu.
      // Permet de styliser dynamiquement : ligne de totaux en gras grisé,
      // et UE partagées en gras + taille supérieure.
      didParseCell: (data) => {
        // Dernière ligne = totaux : fond gris, texte en gras
        if (data.row.index === donnees.length - 1 && data.section === 'body') {
          data.cell.styles.fontStyle = 'bold';
          data.cell.styles.fillColor = '#e2e8f0';
          data.cell.styles.textColor = this.NOIR;
        }
        // UE présente dans plusieurs maquettes : mise en gras
        if (data.section === 'body' && uesPartagees.has(data.row.index)) {
          data.cell.styles.fontStyle = 'bold';
          data.cell.styles.fontSize = 10;
        }
      },
    });

    // finalY = position verticale après le tableau, accessible via lastAutoTable
    return (doc as any).lastAutoTable.finalY + 10;
  }

  // Tableau récapitulatif : volumes horaires, ECTS total,
  // et si les paramètres sont fournis : détail des coûts par type + coût total.
  // Le coût total passe en rouge si le budget est dépassé.
  private dessinerRecapitulatif(
    doc: jsPDF,
    maquette: Maquette,
    parametres: Parametres | undefined,
    y: number,
  ): number {
    doc.setFillColor(this.BLEU_FONCE);
    doc.roundedRect(15, y, 180, 8, 1, 1, 'F');
    doc.setTextColor('#ffffff');
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.text('Récapitulatif', 19, y + 5.5);
    y += 12;

    const lignesRecap: string[][] = [
      ['Volume horaire CM', `${this.getVolumeTotal(maquette, 'cm')}h`],
      ['Volume horaire TD', `${this.getVolumeTotal(maquette, 'td')}h`],
      ['Volume horaire TP', `${this.getVolumeTotal(maquette, 'tp')}h`],
      ['Volume horaire total', `${maquette.volumeHoraireTotal}h`],
      ['ECTS total', `${maquette.ectsTotal}`],
    ];

    if (parametres) {
      const coutCm = this.getVolumeTotal(maquette, 'cm') * parametres.tarifCm;
      const coutTd = this.getVolumeTotal(maquette, 'td') * parametres.tarifTd;
      const coutTp = this.getVolumeTotal(maquette, 'tp') * parametres.tarifTp;
      lignesRecap.push(
        ['', ''],
        [`Coût CM (${this.getVolumeTotal(maquette, 'cm')}h x ${parametres.tarifCm}€)`, `${coutCm} €`],
        [`Coût TD (${this.getVolumeTotal(maquette, 'td')}h x ${parametres.tarifTd}€)`, `${coutTd} €`],
        [`Coût TP (${this.getVolumeTotal(maquette, 'tp')}h x ${parametres.tarifTp}€)`, `${coutTp} €`],
        ['Coût total estimé', `${maquette.coutEstime} €`],
      );
    }

    autoTable(doc, {
      startY: y,
      margin: { left: 15, right: 15 },
      body: lignesRecap,
      theme: 'plain',
      styles: {
        fontSize: 9,
        cellPadding: 3,
        textColor: this.GRIS_TEXTE,
      },
      columnStyles: {
        0: { cellWidth: 120 },
        1: { cellWidth: 57, halign: 'right', fontStyle: 'bold', textColor: this.NOIR },
      },
      // Met en gras les lignes de synthèse (totaux, ECTS, coût).
      // Si le budget est dépassé, le coût total est affiché en rouge.
      didParseCell: (data) => {
        if (data.section === 'body') {
          const raw = data.row.raw as string[];
          const label = raw?.[0] ?? '';
          if (label === 'Volume horaire total' || label === 'ECTS total' || label === 'Coût total estimé') {
            data.cell.styles.fontStyle = 'bold';
            data.cell.styles.textColor = this.NOIR;
            if (label === 'Coût total estimé' && maquette.budgetDepasse) {
              data.cell.styles.textColor = '#dc2626';
            }
          }
        }
      },
    });

    return (doc as any).lastAutoTable.finalY + 5;
  }

  // Ajoute "GestMaquette — Page X/N" centré en bas de chaque page
  private dessinerPiedDePage(doc: jsPDF): void {
    const nbPages = doc.getNumberOfPages();
    for (let i = 1; i <= nbPages; i++) {
      doc.setPage(i);
      doc.setFontSize(8);
      doc.setTextColor('#94a3b8');
      doc.setFont('helvetica', 'normal');
      doc.text(`GestMaquette — Page ${i}/${nbPages}`, 105, 290, { align: 'center' });
    }
  }

  private getVolumeTotal(maquette: Maquette, type: 'cm' | 'td' | 'tp'): number {
    return maquette.semestres.flatMap((s) => s.ues).reduce((sum, ue) => sum + ue[type], 0);
  }
}
