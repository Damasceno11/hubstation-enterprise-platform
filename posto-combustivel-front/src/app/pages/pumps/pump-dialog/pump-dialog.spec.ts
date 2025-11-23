import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PumpDialog } from './pump-dialog';

describe('PumpDialog', () => {
  let component: PumpDialog;
  let fixture: ComponentFixture<PumpDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PumpDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PumpDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
