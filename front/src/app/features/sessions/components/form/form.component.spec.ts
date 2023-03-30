import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent - Given I am on the session create page as an admin', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;

  let serviceTeacher: TeacherService;
  let serviceAPISession: SessionApiService;
  let snackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };
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

  const mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date('2023-02-01T00:00:00'),
      updatedAt: new Date('2023-02-01T00:00:00'),
    },
    {
      id: 2,
      lastName: 'THIERCELIN',
      firstName: 'Hélène',
      createdAt: new Date('2023-02-01T00:00:00'),
      updatedAt: new Date('2023-02-01T00:00:00'),
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: SessionApiService,
          useValue: {
            create: jest.fn().mockReturnValue(of(session)),
            update: jest.fn().mockReturnValue(of(session)),
          },
        },
        {
          provide: TeacherService,
          useValue: {
            all: jest.fn().mockReturnValue(of(mockTeachers)),
          },
        },
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn().mockReturnValue(undefined),
          },
        },
      ],
      imports: [
        RouterTestingModule,
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    serviceTeacher = TestBed.inject(TeacherService);
    serviceAPISession = TestBed.inject(SessionApiService);
    snackBar = TestBed.inject(MatSnackBar);

    fixture.detectChanges();
  });

  it('Then FormComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  it('Then TeacherService should have been called', () => {
    expect(serviceTeacher.all).toHaveBeenCalled();
  });

  it('Then the save button should be disabled and the form invalid', fakeAsync(async () => {
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(submitButton.nativeElement.disabled).toBeTruthy();
    expect(component.sessionForm!.valid).toBeFalsy();
  }));

  describe('When I send the register form with valid credentials', () => {
    it('Then submit method is called, and create session is called, the notification confirm it, and we are redirected to the sessions route', async () => {
      fixture.detectChanges();
      jest.spyOn(component, 'submit');
      jest.spyOn(serviceAPISession, 'create');
      const navigateSpy = jest.spyOn(router, 'navigate');
      // Arrange
      const el = fixture.debugElement.query(By.css('form'));
      fixture.ngZone!.run(() => {
        el.triggerEventHandler('submit', {});
      });
      fixture.detectChanges();
      // Assert
      expect(serviceAPISession.create).toHaveBeenCalled();
      expect(component.submit).toHaveBeenCalled();
      expect(snackBar.open).toHaveBeenCalledWith('Session created !', 'Close', {
        duration: 3000,
      });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });
});

describe('FormComponent - Given I am on the session update page as an admin', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;

  let serviceTeacher: TeacherService;
  let serviceAPISession: SessionApiService;
  let snackBar: MatSnackBar;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };
  const mockServiceAPISession = {
    name: 'Test Session',
    date: new Date('2023-02-01T00:00:00'),
    attendees: 10,
  };
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
  const mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date('2023-02-01T00:00:00'),
      updatedAt: new Date('2023-02-01T00:00:00'),
    },
    {
      id: 2,
      lastName: 'THIERCELIN',
      firstName: 'Hélène',
      createdAt: new Date('2023-02-01T00:00:00'),
      updatedAt: new Date('2023-02-01T00:00:00'),
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      providers: [
        [FormComponent],
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: SessionApiService,
          useValue: {
            update: jest.fn().mockReturnValue(of(session)),
            detail: jest.fn().mockReturnValue(of(mockServiceAPISession)),
          },
        },
        {
          provide: TeacherService,
          useValue: {
            all: jest.fn().mockReturnValue(of(mockTeachers)),
          },
        },
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn().mockReturnValue(undefined),
          },
        },
      ],
      imports: [
        RouterTestingModule,
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    router = TestBed.inject(Router);
    serviceTeacher = TestBed.inject(TeacherService);
    serviceAPISession = TestBed.inject(SessionApiService);
    snackBar = TestBed.inject(MatSnackBar);
    fixture.detectChanges();

    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');
    component.ngOnInit();
  });

  it('Then FormComponent should be created', () => {
    expect(component).toBeTruthy();
  });

  it('Then we should be editing a session', () => {
    expect(router.url).toEqual('/sessions/update/1');
  });

  it('Then TeacherService should have been called', () => {
    expect(serviceTeacher.all).toHaveBeenCalled();
  });

  it('Then the save button should be disabled and the form invalid', fakeAsync(async () => {
    fixture.detectChanges();
    const submitButton = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(submitButton.nativeElement.disabled).toBeTruthy();
    expect(component.sessionForm!.valid).toBeFalsy();
  }));

  describe('When I send the register form with valid credentials', () => {
    it('Then submit method is called, and update session is called, the notification confirm it, and we are redirected to the sessions route', async () => {
      fixture.detectChanges();
      jest.spyOn(component, 'submit');
      jest.spyOn(serviceAPISession, 'update');
      const navigateSpy = jest.spyOn(router, 'navigate');
      // Arrange
      const el = fixture.debugElement.query(By.css('form'));
      fixture.ngZone!.run(() => {
        el.triggerEventHandler('submit', {});
      });
      fixture.detectChanges();
      // Assert
      expect(serviceAPISession.update).toHaveBeenCalled();
      expect(component.submit).toHaveBeenCalled();
      expect(snackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', {
        duration: 3000,
      });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });
});

describe('FormComponent - Given I am on the session create/update page as a user', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let spyNavigate: any;

  const mockSessionService = {
    sessionInformation: {
      admin: false,
    },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      providers: [
        [FormComponent],
        { provide: SessionService, useValue: mockSessionService },
      ],
      imports: [
        RouterTestingModule,
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
    spyNavigate = jest.spyOn(router, 'navigate');
    fixture.ngZone!.run(() => {
      component.ngOnInit();
    });
  });

  it('Then FormComponent should be created and I should be redirected to sessiosn page', () => {
    expect(component).toBeTruthy();
    fixture.detectChanges();
    expect(spyNavigate).toHaveBeenCalledWith(['/sessions']);
  });
});
