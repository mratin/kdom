/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { KdomServiceService } from './kdom-service.service';

describe('KdomServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KdomServiceService]
    });
  });

  it('should ...', inject([KdomServiceService], (service: KdomServiceService) => {
    expect(service).toBeTruthy();
  }));
});
