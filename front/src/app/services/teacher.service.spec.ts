import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';

describe('TeacherService - Given the teacher service is used', () => {
  const httpClientMock = {
    get: jest.fn(),
  };
  let teacherService: TeacherService;

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
    teacherService = TestBed.inject(TeacherService);
    jest.clearAllMocks();
  });

  it('Then teacherService should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  describe('When detail method is called', () => {
    it('Should make a GET request to the detail endpoint', () => {
      teacherService.detail('1');
      expect(httpClientMock.get).toHaveBeenCalledWith('api/teacher/1');
    });
  });
  describe('When all method is called', () => {
    it('Should make a GET request to the all endpoint', () => {
      teacherService.all();
      expect(httpClientMock.get).toHaveBeenCalledWith('api/teacher');
    });
  });
});
