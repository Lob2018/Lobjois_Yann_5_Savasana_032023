import { HttpClientModule } from '@angular/common/http';
import { Component, DebugElement } from '@angular/core';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { AppComponent } from './app.component';

describe('AppComponent - Given I am on the root page', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let toolbar: DebugElement[];

  let router: Router;

  // Mock called components
  @Component({
    selector: 'app-login',
    template: '',
  })
  class MockLoginComponent {}
  @Component({
    selector: 'app-register',
    template: '',
  })
  class MockRegisterComponent {}

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: MockLoginComponent },
          { path: 'register', component: MockRegisterComponent },
        ]),
        HttpClientModule,
        MatToolbarModule,
        ReactiveFormsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
    toolbar = fixture.debugElement.queryAll(By.css('span'));
  });
  it('Then AppComponent should be created', () => {
    expect(component).toBeTruthy();
  });
  it('Then by default we are not connected', () => {
    expect(component.$isLogged()).toBeTruthy();
  });
  it('Then the application name, Login and Register should be shown', fakeAsync(async () => {
    fixture.detectChanges();
    expect(toolbar[0].nativeElement.textContent).toContain('Yoga app');
    expect(toolbar[1].nativeElement.textContent).toContain('Login');
    expect(toolbar[2].nativeElement.textContent).toContain('Register');
  }));
  describe('When I click on Login button', () => {
    it('Then I am redirected on the login page', fakeAsync(async () => {
      fixture.detectChanges();
      toolbar[1].nativeElement.click();
      tick(500);
      fixture.detectChanges();
      expect(router.url).toBe('/login');
    }));
  });
  describe('When I click on Register button', () => {
    it('Then I am redirected on the register page', fakeAsync(async () => {
      fixture.detectChanges();
      toolbar[2].nativeElement.click();
      tick(500);
      fixture.detectChanges();
      expect(router.url).toBe('/register');
    }));
  });
  describe('When I am logged', () => {
    it('Then the application name, Sessions Account and Logout should be shown', fakeAsync(async () => {
      jest.spyOn(component, '$isLogged').mockReturnValue(of(true));
      tick(500);
      fixture.detectChanges();
      let toolbar = fixture.debugElement.queryAll(By.css('span'));

      expect(component.$isLogged()).toBeTruthy();
      expect(toolbar[0].nativeElement.textContent).toContain('Yoga app');
      expect(toolbar[1].nativeElement.textContent).toContain('Sessions');
      expect(toolbar[2].nativeElement.textContent).toContain('Account');
      expect(toolbar[3].nativeElement.textContent).toContain('Logout');
    }));
  });
  describe('When I am logged and I click on Logout button', () => {
    it('Then I am disconnected and redirected to the landing page', fakeAsync(async () => {
      jest.spyOn(component, '$isLogged').mockReturnValue(of(true));
      const logoutSpy = jest.spyOn(component, 'logout');
      tick(500);
      fixture.detectChanges();
      let toolbar = fixture.debugElement.queryAll(By.css('span'));
      expect(component.$isLogged()).toBeTruthy();

      toolbar[3].nativeElement.click();
      tick(500);
      fixture.detectChanges();
      expect(logoutSpy).toHaveBeenCalled();
      expect(router.url).toBe('/');
    }));
  });

  describe('When I am logged', () => {
    it('Then the application name, Sessions Account and Logout should be shown', fakeAsync(async () => {
      jest.spyOn(component, '$isLogged').mockReturnValue(of(true));
      tick(500);
      fixture.detectChanges();
      let toolbar = fixture.debugElement.queryAll(By.css('span'));

      expect(component.$isLogged()).toBeTruthy();
      expect(toolbar[0].nativeElement.textContent).toContain('Yoga app');
      expect(toolbar[1].nativeElement.textContent).toContain('Sessions');
      expect(toolbar[2].nativeElement.textContent).toContain('Account');
      expect(toolbar[3].nativeElement.textContent).toContain('Logout');
    }));
  });
});
