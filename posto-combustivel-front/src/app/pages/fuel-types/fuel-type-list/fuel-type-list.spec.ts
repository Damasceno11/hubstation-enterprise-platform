import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelTypeList } from './fuel-type-list';

describe('FuelTypeList', () => {
  let component: FuelTypeList;
  let fixture: ComponentFixture<FuelTypeList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelTypeList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FuelTypeList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
