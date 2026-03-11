import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UE } from './ue';

describe('UE', () => {
  let component: UE;
  let fixture: ComponentFixture<UE>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UE]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UE);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
