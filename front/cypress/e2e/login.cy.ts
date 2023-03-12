import { login } from '../support/utils/common';

describe('Login spec', () => {
  it('Login successfull', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    login('adminLogin', 'yoga@studio.com', 'test!1234');

    cy.wait('@session').url().should('include', '/sessions');
  });
});
