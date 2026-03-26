import { Semestre } from './semestre';

export interface Maquette {
  id: number;
  nomMaquette: string;
  typeMaquette: 'LICENCE' | 'MASTER';
  semestres: Semestre[];
  nbSemestres: number;
  ectsTotal: number;
  volumeHoraireTotal: number;
  nbUeTotal: number;
  respecteEcts: boolean;
  ectsManquants: number;
  ectsSurplus: number;
  coutEstime: number;
  budgetMax: number;
  budgetDepasse: boolean;
}
