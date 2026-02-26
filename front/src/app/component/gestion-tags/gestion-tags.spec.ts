import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionTags } from './gestion-tags';

describe('GestionTags', () => {
  let component: GestionTags;
  let fixture: ComponentFixture<GestionTags>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionTags]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionTags);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
