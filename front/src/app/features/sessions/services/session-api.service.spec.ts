import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionsService - Given the sessions service is used', () => {
  const httpClientMock = {
    get: jest.fn(),
    delete: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
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

  let sessionsSevice: SessionApiService;

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
    sessionsSevice = TestBed.inject(SessionApiService);
    jest.clearAllMocks();
  });

  it('Then sessionsSevice should be created', () => {
    expect(sessionsSevice).toBeTruthy();
  });

  describe('When delete method is called', () => {
    it('Should make a DELETE request to the delete endpoint', () => {
      sessionsSevice.delete('1');
      expect(httpClientMock.delete).toHaveBeenCalledWith('api/session/1');
    });
  });
  describe('When detail method is called', () => {
    it('Should make a GET request to the detail endpoint', () => {
      sessionsSevice.detail('1');
      expect(httpClientMock.get).toHaveBeenCalledWith('api/session/1');
    });
  });
  describe('When participate method is called', () => {
    it('Should make a POST request to the participate endpoint', () => {
      sessionsSevice.participate('1', '2');
      expect(httpClientMock.post).toHaveBeenCalledWith(
        'api/session/1/participate/2',
        null
      );
    });
  });
  describe('When unparticipate method is called', () => {
    it('Should make a POST request to the unparticipate endpoint', () => {
      sessionsSevice.unParticipate('1', '2');
      expect(httpClientMock.delete).toHaveBeenCalledWith(
        'api/session/1/participate/2'
      );
    });
  });
  describe('When create method is called', () => {
    it('Should make a POST request to the create endpoint', () => {
      sessionsSevice.create(session);
      expect(httpClientMock.post).toHaveBeenCalledWith('api/session', session);
    });
  });
  describe('When update method is called', () => {
    it('Should make a PUT request to the update endpoint', () => {
      sessionsSevice.update('1', session);
      expect(httpClientMock.put).toHaveBeenCalledWith('api/session/1', session);
    });
  });
});
