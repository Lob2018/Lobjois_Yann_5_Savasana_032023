import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { SessionsModule } from 'src/app/features/sessions/sessions.module';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { AuthService } from '../../services/auth.service';

import { LoginComponent } from './login.component';

describe('LoginComponent - Given I am on the login page', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let serviceAuthLogin: AuthService;
  let serviceSession: SessionService;

  let router: Router;

  const loginRequest: LoginRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234',
  };
  const mockedLoginResponse200: SessionInformation = {
    token:
      'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2Nzg5NjQwNjQsImV4cCI6MTY3OTA1MDQ2NH0.ZHTp0XHK6IaWLzTKLHwyJXeDOAsyu9RPLgUBUjaniDulLLttRlWP-nVfZCpjIqn6Zb-xAumtPPLVxR9L2L17gA',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: SessionsModule },
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    serviceAuthLogin = TestBed.inject(AuthService);
    serviceSession = TestBed.inject(SessionService);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('Then LoginComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  it('Then the submit button should be disabled and the form invalid', fakeAsync(async () => {
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(submitButton.nativeElement.disabled).toBeTruthy();
    expect(component.form.valid).toBeFalsy();
  }));

  describe('When I click on the password eye icon', () => {
    it('Then password type toggle from password to text', fakeAsync(async () => {
      fixture.detectChanges();
      const toggleButton = fixture.debugElement.query(
        By.css('button[aria-label="Hide password"]')
      );
      const password = fixture.debugElement.nativeElement.querySelector(
        'input[formcontrolname="password"]'
      );
      expect(password.type).toBe('password');
      toggleButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();
      expect(password.type).toBe('text');
      toggleButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();
      expect(password.type).toBe('password');
    }));
  });
  describe('When I fill the login form with an invalid email', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const email = component.form.controls['email'];
      email.setValue('yogastudio.com');
      const password = component.form.controls['password'];
      password.setValue(loginRequest.password);
      tick(1000);
      fixture.detectChanges();
      expect(component.form.valid).toBeFalsy();
    }));
  });
  describe('When I fill the login form with no password', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const email = component.form.controls['email'];
      email.setValue(loginRequest.email);
      const password = component.form.controls['password'];
      password.setValue('');
      tick(1000);
      fixture.detectChanges();
      expect(component.form.valid).toBeFalsy();
    }));
  });
  describe('When I send the login form with invalid credentials', () => {
    it('Then we should get a message, and the router is not called', async () => {
      fixture.detectChanges();
      const navigateSpy = jest.spyOn(router, 'navigate');
      jest
        .spyOn(serviceAuthLogin, 'login')
        .mockReturnValue(throwError(() => new Error('')));

      serviceAuthLogin.login({ email: '', password: '' }).subscribe();

      // Arrange
      const el = fixture.debugElement.query(By.css('.login-form'));
      el.triggerEventHandler('submit', {});

      // Assert
      fixture.detectChanges();

      const msg = fixture.debugElement.query(By.css('.error')).nativeElement;
      expect(msg).toBeTruthy();
      expect(navigateSpy).not.toBeCalled();
    });
  });
  describe('When I send the login form with valid credentials', () => {
    it('Then submit method is called and we are redirected to /sessions', async () => {
      fixture.detectChanges();
      const navigateSpy = jest.spyOn(router, 'navigate');
      jest.spyOn(component, 'submit');
      jest.spyOn(serviceSession, 'logIn');
      jest
        .spyOn(serviceAuthLogin, 'login')
        .mockReturnValue(of(mockedLoginResponse200));

      serviceSession.logIn(mockedLoginResponse200);
      serviceAuthLogin.login(loginRequest).subscribe();

      // Arrange
      const el = fixture.debugElement.query(By.css('.login-form'));
      el.triggerEventHandler('submit', {});
      fixture.detectChanges();

      // Assert
      expect(component.submit).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    });
  });
});
