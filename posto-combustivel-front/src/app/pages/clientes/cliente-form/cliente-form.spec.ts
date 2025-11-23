import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroFidelidade } from './cliente-form';

describe('CadastroFidelidade', () => {
  let component: CadastroFidelidade;
  let fixture: ComponentFixture<CadastroFidelidade>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastroFidelidade],
    }).compileComponents();

    fixture = TestBed.createComponent(CadastroFidelidade);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
