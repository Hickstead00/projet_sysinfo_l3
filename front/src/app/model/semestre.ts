import { Ue } from './ue';

export interface Semestre {
  id: number;
  numeroSemestre: number;
  ues: Ue[];
  nbUe: number;
  ectsTotal: number;
  volumeHoraireCm: number;
  volumeHoraireTd: number;
  volumeHoraireTp: number;
  volumeHoraireTotal: number;
  respecteEcts: boolean;
  ectsManquants: number;
  ectsSurplus: number;
}
