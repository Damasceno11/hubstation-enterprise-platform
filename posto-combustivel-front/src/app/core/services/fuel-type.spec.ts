import { TestBed } from '@angular/core/testing';

import { FuelType } from './fuel-type';

describe('FuelType', () => {
  let service: FuelType;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuelType);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
