import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelTypeDialog } from './fuel-type-dialog';

describe('FuelTypeDialog', () => {
  let component: FuelTypeDialog;
  let fixture: ComponentFixture<FuelTypeDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelTypeDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelTypeDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
