/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { NewGamesComponent } from './new-games.component';

describe('NewGamesComponent', () => {
  let component: NewGamesComponent;
  let fixture: ComponentFixture<NewGamesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewGamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewGamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
