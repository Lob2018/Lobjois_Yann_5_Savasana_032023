import { login } from '../support/utils/common';

describe('User account spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      { fixture: 'sessions' }
    ).as('session');
  });

  it('Administrator account information is displayed for the administrator', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      { fixture: 'adminAccount' }
    ).as('information');
    login('adminLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session').get('span[routerlink="me"]').click();
    cy.contains('button', 'Detail').should('not.exist');
    cy.log('**No Detail button to delete this account**');
    cy.fixture('adminAccount').then((json) => {
      const name = json.firstName + ' ' + json.lastName.toUpperCase();
      cy.contains('p', name);
      cy.log('**The admin name is shown**');
      cy.contains('p', json.email);
      cy.log('**The admin email is shown**');
      cy.contains('p', 'You are admin');
      cy.log('**The admin message is shown**');
    });
  });

  it('User account information and delete account button is displayed for user', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      { fixture: 'userAccount' }
    ).as('information');
    login('userLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session').get('span[routerlink="me"]').click();
    cy.contains('button', 'Detail');
    cy.log('**The button to delete this account exists**');
    cy.fixture('userAccount').then((json) => {
      const name = json.firstName + ' ' + json.lastName.toUpperCase();
      cy.contains('p', name);
      cy.log('**The user name is shown**');
      cy.contains('p', json.email);
      cy.log('**The user email is shown**');
    });
  });

  it('Administrator back from account information successfull', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      { fixture: 'adminAccount' }
    ).as('information');
    login('adminLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session').get('span[routerlink="me"]').click();
    cy.contains('button', 'arrow_back').click();
    cy.wait('@session').url().should('include', '/sessions');
    cy.log('**Back to the sessions list successfull**');
  });
});
