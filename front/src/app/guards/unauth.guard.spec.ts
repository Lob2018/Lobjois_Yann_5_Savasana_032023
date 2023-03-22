import { UnauthGuard } from './unauth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

describe('UnauthGuard - Given I navigate to the root route', () => {
  let unauthGuard: UnauthGuard;
  let router: Router;
  let serviceSession: SessionService;

  beforeEach(() => {
    router = {
      navigate: jest.fn(),
    } as any;
    serviceSession = {
      isLogged: false,
    } as any;
    unauthGuard = new UnauthGuard(router, serviceSession);
  });

  it('Should allow access if user is not logged in', () => {
    const canActivate = unauthGuard.canActivate();
    expect(canActivate).toBe(true);
  });

  it('Should redirect to rentals if user is logged in', () => {
    serviceSession.isLogged = true;
    const canActivate = unauthGuard.canActivate();
    expect(canActivate).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['rentals']);
  });
});
