import { login } from '../support/utils/common';

describe('Logout spec', () => {
  it('Logout successfull', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    login('userLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session').get('span').contains('Logout').click();

    cy.location().then((loc) => {
      cy.get('span').contains('Login');
      cy.log('**The Login button exists**');
      cy.url().should('eq', loc.protocol+'//' + loc.host + '/');
      cy.log('**This is the URL of the login page**');
    });
  });
});
