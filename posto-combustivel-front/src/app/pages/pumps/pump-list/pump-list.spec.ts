import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PumpList } from './pump-list';

describe('PumpList', () => {
  let component: PumpList;
  let fixture: ComponentFixture<PumpList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PumpList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PumpList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
