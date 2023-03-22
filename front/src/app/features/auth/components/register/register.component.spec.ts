import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
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
import { SessionService } from 'src/app/services/session.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthService } from '../../services/auth.service';

import { RegisterComponent } from './register.component';

describe('RegisterComponent - Given I am on the register page', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let serviceAuthRegister: AuthService;
  let serviceSession: SessionService;
  let router: Router;

  const registerRequest: RegisterRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234',
    firstName: 'yoga',
    lastName: 'studio',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: SessionsModule },
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    serviceAuthRegister = TestBed.inject(AuthService);
    serviceSession = TestBed.inject(SessionService);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('Then RegisterComponent should be created', () => {
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

  describe('When I fill the register form with an invalid firstname (at least one character)', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const firstname = component.form.controls['firstName'];
      firstname.setValue('y');
      tick(500);
      fixture.detectChanges();
      expect(firstname.valid).toBeTruthy();
      firstname.setValue('');
      tick(500);
      fixture.detectChanges();
      expect(firstname.valid).toBeFalsy();
    }));
  });
  describe('When I fill the register form with an invalid lastname (at least one character)', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const lastname = component.form.controls['lastName'];
      lastname.setValue('y');
      tick(500);
      fixture.detectChanges();
      expect(lastname.valid).toBeTruthy();
      lastname.setValue('');
      tick(500);
      fixture.detectChanges();
      expect(lastname.valid).toBeFalsy();
    }));
  });
  describe('When I fill the register form with an invalid email', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const email = component.form.controls['email'];
      email.setValue(registerRequest.email);
      tick(500);
      fixture.detectChanges();
      expect(email.valid).toBeTruthy();
      email.setValue('yogastudio.com');
      tick(500);
      fixture.detectChanges();
      expect(email.valid).toBeFalsy();
    }));
  });
  describe('When I fill the register form with an invalid password (at least two characters)', () => {
    it('Then the form is invalid', fakeAsync(async () => {
      fixture.detectChanges();
      const password = component.form.controls['password'];
      password.setValue('te');
      tick(500);
      fixture.detectChanges();
      expect(password.valid).toBeTruthy();
      password.setValue('');
      tick(500);
      fixture.detectChanges();
      expect(password.valid).toBeFalsy();
    }));
  });
  describe('When I send the register form with invalid credentials', () => {
    it('Then we should get a message, and the router is not called', async () => {
      fixture.detectChanges();
      const navigateSpy = jest.spyOn(router, 'navigate');
      jest
        .spyOn(serviceAuthRegister, 'register')
        .mockReturnValue(throwError(() => new Error('')));

      serviceAuthRegister
        .register({ firstName: '', lastName: '', email: '', password: '' })
        .subscribe();

      // Arrange
      const el = fixture.debugElement.query(By.css('.register-form'));
      el.triggerEventHandler('submit', {});

      // Assert
      fixture.detectChanges();

      const msg = fixture.debugElement.query(By.css('.error')).nativeElement;
      expect(msg).toBeTruthy();
      expect(navigateSpy).not.toBeCalled();
    });
  });
  describe('When I send the register form with valid credentials', () => {
    it('Then submit method is called and we are redirected to /login', async () => {
      fixture.detectChanges();
      const navigateSpy = jest.spyOn(router, 'navigate');
      jest.spyOn(component, 'submit');
      jest.spyOn(serviceSession, 'logIn');

      jest
        .spyOn(serviceAuthRegister, 'register')
        .mockReturnValue(of(undefined));

      serviceAuthRegister.register(registerRequest).subscribe();

      // Arrange
      const el = fixture.debugElement.query(By.css('.register-form'));
      el.triggerEventHandler('submit', {});
      fixture.detectChanges();

      // Assert
      expect(component.submit).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    });
  });
});
