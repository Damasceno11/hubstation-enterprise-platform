import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplyDialog } from './supply-dialog';

describe('SupplyDialog', () => {
  let component: SupplyDialog;
  let fixture: ComponentFixture<SupplyDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupplyDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplyDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
