import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UeService } from '../../service/ue-service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { installDevToolsSignalFormatter } from '@angular/core/primitives/signals';

@Component({
  selector: 'app-ue',
  imports: [],
  templateUrl: './ue.html',
  styleUrl: './ue.scss',
})
export class UE implements OnInit {
  listUe: UE[] = [];

  tagForm!: FormGroup;

  constructor(
    private ueService: UeService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.allUe();
  }

  initForm() {
    this.tagForm = this.fb.group({});
  }

  allUe(): void {
    this.ueService.getAllUe().subscribe({
      next: (ue) => {
        this.listUe = ue;
        this.cdr.detectChanges();
      },
    });
  }
}
