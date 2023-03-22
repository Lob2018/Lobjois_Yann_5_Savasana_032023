import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService - Given the user service is used', () => {
  const httpClientMock = {
    get: jest.fn(),
    delete: jest.fn(),
  };
  let userService: UserService;

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
    userService = TestBed.inject(UserService);
    jest.clearAllMocks();
  });

  it('Then userService should be created', () => {
    expect(userService).toBeTruthy();
  });

  describe('When getById method is called', () => {
    it('Should make a GET request to the get by id endpoint', () => {
      userService.getById('1');
      expect(httpClientMock.get).toHaveBeenCalledWith('api/user/1');
    });
  });

  describe('When delete method is called', () => {
    it('Should make a DELETE request to the delete endpoint', () => {
      userService.delete('1');
      expect(httpClientMock.delete).toHaveBeenCalledWith('api/user/1');
    });
  });
});
