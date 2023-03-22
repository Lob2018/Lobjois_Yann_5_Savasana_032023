import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { User } from 'src/app/interfaces/user.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { UserService } from 'src/app/services/user.service';
import { SessionService } from '../../../../services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { SessionsModule } from '../../sessions.module';

import { DetailComponent } from './detail.component';

const session: Session = {
  id: 1,
  name: 'name',
  description: 'description',
  date: new Date('2023-02-01T00:00:00'),
  teacher_id: 1,
  users: [2, 1],
  createdAt: new Date('2023-02-01T00:00:00'),
  updatedAt: new Date('2023-02-01T00:00:00'),
};
const user: User = {
  id: 1,
  email: 'test@test.fr',
  lastName: 'user',
  firstName: 'user',
  admin: false,
  password: 'test',
  createdAt: new Date('2023-02-01T00:00:00'),
};
const userUnparticpate: User = {
  id: 3,
  email: 'test@test.fr',
  lastName: 'user',
  firstName: 'user',
  admin: false,
  password: 'test',
  createdAt: new Date('2023-02-01T00:00:00'),
};

const admin: User = {
  id: 1,
  email: 'test@test.fr',
  lastName: 'user',
  firstName: 'user',
  admin: true,
  password: 'test',
  createdAt: new Date('2023-02-01T00:00:00'),
};

const teacher: Teacher = {
  id: 1,
  lastName: 'teacher',
  firstName: 'teacher',
  createdAt: new Date('2023-02-01T00:00:00'),
  updatedAt: new Date('2023-02-01T00:00:00'),
};

describe('DetailComponent - Given I am on the detail page as a user and I participate in the session', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let serviceUser: UserService;
  let serviceSession: SessionService;
  let serviceAPISession: SessionApiService;
  let snackBar: MatSnackBar;

  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
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
          },
        },
        {
          provide: SessionApiService,
          useValue: {
            sessionId: jest.fn().mockReturnValue('1'),
            detail: jest.fn().mockReturnValue(of(session)),
            participate: jest.fn(() => of(null)),
            unParticipate: jest.fn(() => of(null)),
          },
        },
        {
          provide: TeacherService,
          useValue: {
            detail: jest.fn().mockReturnValue(of(teacher)),
          },
        },
      ],
    }).compileComponents();
    router = TestBed.inject(Router);
    serviceUser = TestBed.inject(UserService);
    serviceSession = TestBed.inject(SessionService);
    serviceAPISession = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('Then DetailComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('When I click on the back icon', () => {
    it('Then history back should be called', fakeAsync(async () => {
      fixture.detectChanges();
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

  describe('When I click on the unparticipate button', () => {
    it('Then unparticipate method should be called', fakeAsync(async () => {
      const participateSpy = jest.spyOn(serviceAPISession, 'unParticipate');
      fixture.detectChanges();
      const participateButton = fixture.debugElement.query(
        By.css('button[mat-raised-button]')
      );
      expect(participateButton.nativeElement.textContent).toBe(
        'person_removeDo not participate'
      );
      participateButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();
      expect(participateSpy).toHaveBeenCalled();
    }));
  });
});

describe('DetailComponent - Given I am on the detail page as a user and I do not participate in the session', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let serviceUser: UserService;
  let serviceSession: SessionService;
  let serviceAPISession: SessionApiService;
  let snackBar: MatSnackBar;

  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
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
            sessionInformation: userUnparticpate,
          },
        },
        {
          provide: SessionApiService,
          useValue: {
            sessionId: jest.fn().mockReturnValue('1'),
            detail: jest.fn().mockReturnValue(of(session)),
            participate: jest.fn(() => of(null)),
            unParticipate: jest.fn(() => of(null)),
          },
        },
        {
          provide: TeacherService,
          useValue: {
            detail: jest.fn().mockReturnValue(of(teacher)),
          },
        },
      ],
    }).compileComponents();
    router = TestBed.inject(Router);
    serviceUser = TestBed.inject(UserService);
    serviceSession = TestBed.inject(SessionService);
    serviceAPISession = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('Then DetailComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('When I click on the participate button', () => {
    it('Then participate method should be called', fakeAsync(async () => {
      const participateSpy = jest.spyOn(serviceAPISession, 'participate');
      fixture.detectChanges();
      const participateButton = fixture.debugElement.query(
        By.css('button[mat-raised-button]')
      );
      expect(participateButton.nativeElement.textContent).toBe(
        'person_addParticipate'
      );
      participateButton.triggerEventHandler('click', null);
      tick(500);
      fixture.detectChanges();
      expect(participateSpy).toHaveBeenCalled();
    }));
  });
});

describe('DetailComponent - Given I am on the detail page as an admin', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let serviceUser: UserService;
  let serviceSession: SessionService;
  let serviceAPISession: SessionApiService;
  let snackBar: MatSnackBar;

  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: SessionsModule },
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
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
            sessionInformation: admin,
          },
        },
        {
          provide: SessionApiService,
          useValue: {
            sessionId: jest.fn().mockReturnValue('1'),
            detail: jest.fn().mockReturnValue(of(session)),
            delete: jest.fn().mockReturnValue(of(undefined)),
          },
        },
        {
          provide: TeacherService,
          useValue: {
            detail: jest.fn().mockReturnValue(of(teacher)),
          },
        },
      ],
    }).compileComponents();
    router = TestBed.inject(Router);
    serviceUser = TestBed.inject(UserService);
    serviceSession = TestBed.inject(SessionService);
    serviceAPISession = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();
  });

  it('Then DetailComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  describe('When I click on the delete icon', () => {
    it('Then delete method should be called', fakeAsync(async () => {
      const navigateSpy = jest.spyOn(router, 'navigate');
      const deleteSpy = jest.spyOn(serviceAPISession, 'delete');
      fixture.detectChanges();
      const deleteButton = fixture.debugElement.query(
        By.css('button[mat-raised-button]')
      );
      expect(deleteButton.nativeElement.textContent).toBe('deleteDelete');
      deleteButton.triggerEventHandler('click', null);
      fixture.detectChanges();
      expect(deleteSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
      expect(snackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', {
        duration: 3000,
      });
    }));
  });
});
