import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

import { AuthService } from './auth.service';

describe('AuthService - Given the auth service is used', () => {
  const httpClientMock = {
    post: jest.fn(),
  };
  let authService: AuthService;

  const registerRequest: RegisterRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234',
    firstName: 'yoga',
    lastName: 'studio',
  };

  const loginRequest: LoginRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234',
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        {
          provide: HttpClient,
          useValue: httpClientMock,
        },
      ],
    });
    authService = TestBed.inject(AuthService);
    jest.clearAllMocks();
  });

  it('Then AuthService should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('When register method is called', () => {
    it('Should make a POST request to the registration endpoint', () => {
      authService.register(registerRequest);
      expect(httpClientMock.post).toHaveBeenCalledWith(
        'api/auth/register',
        registerRequest
      );
    });
  });

  describe('When login method is called', () => {
    it('Should make a POST request to the login endpoint', () => {
      authService.login(loginRequest);
      expect(httpClientMock.post).toHaveBeenCalledWith(
        'api/auth/login',
        loginRequest
      );
    });
  });
});
