export interface CreateUe {
  nomUe: string;
  ects: number;
  cm: number;
  td: number;
  tp: number;
  description: string;
  ueObligatoire: boolean;
  tagIds: number[];
  enseignantIds: number[];
  referentIds: number[];
  prerequisIds: number[];
}
