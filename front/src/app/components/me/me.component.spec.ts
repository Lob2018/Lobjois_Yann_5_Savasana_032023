import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { AppModule } from 'src/app/app.module';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';

import { MeComponent } from './me.component';

describe('MeComponent - Given I am on the me page', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let serviceUser: UserService;
  let serviceSession: SessionService;
  let snackBar: MatSnackBar;

  let router: Router;

  const user: User = {
    id: 1,
    email: 'test@test.fr',
    lastName: 'test',
    firstName: 'test',
    admin: false,
    password: 'test',
    createdAt: new Date('2023-02-01T00:00:00'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule.withRoutes([{ path: '', component: AppModule }]),
        BrowserAnimationsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn().mockReturnValue(undefined),
          },
        },
        {
          provide: SessionService,
          useValue: {
            sessionInformation: user,
            logOut: jest.fn(),
          },
        },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(user)),
            delete: jest.fn().mockReturnValue(of(user)),
          },
        },
      ],
    }).compileComponents();
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    serviceSession = TestBed.inject(SessionService);
    serviceUser = TestBed.inject(UserService);
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('Then MeComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('When I click on the back icon', () => {
    it('Then history back should be called', fakeAsync(async () => {
      const backSpy = jest.spyOn(window.history, 'back');
      fixture.detectChanges();
      const backButton = fixture.debugElement.query(
        By.css('button[mat-icon-button]')
      );
      backButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();
      expect(backSpy).toHaveBeenCalled();
    }));
  });

  describe('When I am logged as a user, and I click on the Detail button', () => {
    it('Then the user should be deleted, and the snackbar should be "Your account has been deleted !", and logout should be called ', fakeAsync(async () => {
      fixture.detectChanges();

      const navigateSpy = jest.spyOn(router, 'navigate');
      const logOutSpy = jest.spyOn(serviceSession, 'logOut');
      const deleteSpy = jest.spyOn(serviceUser, 'delete');

      // snackbar
      jest.spyOn(snackBar, 'open');

      const detailButton = fixture.debugElement.query(
        By.css('button[color="warn"]')
      );
      detailButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();

      expect(logOutSpy).toHaveBeenCalled();
      expect(deleteSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['/']);

      expect(snackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3000 }
      );
    }));
  });
});
