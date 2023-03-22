import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService - Given the session service is used', () => {
  let sessionSevice: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionSevice = TestBed.inject(SessionService);
  });

  it('Then sessionSevice should be created', () => {
    expect(sessionSevice).toBeTruthy();
  });
});
