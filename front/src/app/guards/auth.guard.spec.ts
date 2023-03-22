import { AuthGuard } from './auth.guard';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

describe('AuthGuard - Given I navigate to a protected route', () => {
  let authGuard: AuthGuard;
  let router: Router;
  let serviceSeesion: SessionService;

  beforeEach(() => {
    router = {
      navigate: jest.fn(),
    } as any;
    serviceSeesion = {
      isLogged: false,
    } as any;
    authGuard = new AuthGuard(router, serviceSeesion);
  });

  it('Should redirect to login if user is not logged in', () => {
    const canActivate = authGuard.canActivate();
    expect(canActivate).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['login']);
  });

  it('Should allow access if user is logged in', () => {
    serviceSeesion.isLogged = true;
    const canActivate = authGuard.canActivate();
    expect(canActivate).toBe(true);
  });
});
