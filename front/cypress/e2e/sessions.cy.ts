import { login } from '../support/utils/common';
import { sessionDisplayDate, sessionSetDate } from '../support/utils/date';

describe('Sessions spec', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      { fixture: 'sessions' }
    ).as('session');
  });

  it('Sessions are shown for user', () => {
    login('userLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session')
      .get('mat-card')
      .find('.item')
      .its('length')
      .should('be.gte', 1);
  });

  it('Sessions are displayed for admin with create and details buttons', () => {
    login('adminLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session')
      .get('mat-card')
      .find('.item')
      .its('length')
      .should('be.gte', 1);
    cy.contains('button', 'Create');
    cy.log('**The create button exists**');
    cy.get('mat-card')
      .find('.item')
      .each(($el, index, $list) => {
        cy.log(
          '**The Detail button for the session ' + (index + 1) + ' exists**'
        );
        cy.wrap($el).contains('button', 'Detail');
      });
  });

  it('Admin create session successfull', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      { fixture: 'teachers' }
    ).as('teachers');

    cy.intercept(
      {
        method: 'POST',
        url: '/api/session',
      },
      { fixture: 'sessionCreated' }
    ).as('sessionCreated');

    login('adminLogin', 'yoga@studio.com', 'test!1234');
    cy.wait('@session').get('span').contains('Create').click();

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      { fixture: 'sessionRead' }
    ).as('sessionRead');

    cy.wait('@teachers').url().should('include', '/create');
    cy.log('**The session form is shown**');

    cy.fixture('sessionCreated').then((json) => {
      cy.get('input[formControlName=name]').type(json.name);
      cy.get('input[formControlName=date]').type(json.date);
      cy.get('textarea[formControlName=description]').type(json.description);
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('#mat-option-1').click();
      cy.contains('button', 'Save').click();
      cy.log('**The session form is completed and sent**');
    });

    cy.fixture('sessionRead').then((json) => {
      cy.url().should('include', '/sessions');
      cy.get('mat-card-title').last().should('contain', json[0].name);
      cy.log('**The session created is shown**');
    });
  });

  it('Session informations are shown for user', () => {
    login('userLogin', 'yoga@studio.com', 'test!1234');

    cy.fixture('sessions').then((json) => {
      cy.wait('@session')
        .get('.mat-card-title')
        .last()
        .should('contain', json[1].name);

      cy.get('.mat-card-subtitle')
        .last()
        .should('contain', sessionDisplayDate(new Date(json[1].date)));
      cy.get('.mat-card-content').last().should('contain', json[1].description);
    });
  });

  it('Update a session as admin', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      { fixture: 'teachers' }
    ).as('teachers');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/3',
      },
      { fixture: 'sessionBeforeUpdate' }
    ).as('sessionBeforeUpdate');

    cy.intercept(
      {
        method: 'PUT',
        url: '/api/session/3',
      },
      { fixture: 'sessionAfterUpdate' }
    ).as('sessionAfterUpdate');

    login('adminLogin', 'yoga@studio.com', 'test!1234');

    cy.wait('@session')
      .get('.mat-card')
      .last()
      .contains('button', 'Edit')
      .click();

    cy.fixture('sessionAfterUpdate').then((json) => {
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        { fixture: 'sessionReadUpdated' }
      ).as('sessionUpdated');
      cy.wait('@sessionBeforeUpdate')
        .get('input[formControlName=name]')
        .type(json.name);
      cy.get('input[formControlName=date]').type(
        sessionSetDate(new Date(json.date))
      );
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('#mat-option-0').click();
      cy.get('textarea[formControlName=description]').type(json.description);
      cy.contains('button', 'Save').click();
      cy.log('**The session form is updated and sent**');

      cy.wait('@sessionUpdated')
        .get('.mat-card-title')
        .last()
        .should('contain', json.name);
      cy.get('.mat-card-subtitle')
        .last()
        .should('contain', sessionDisplayDate(new Date(json.date)));
      cy.get('.mat-card-content').last().should('contain', json.description);
      cy.log('**The session information is updated**');
    });
  });

  it('Delete a session as admin', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/3',
      },
      { fixture: 'sessionBeforeUpdate' }
    ).as('sessionBeforeUpdate');

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/3',
      },
      {}
    ).as('sessionDeleted');

    login('adminLogin', 'yoga@studio.com', 'test!1234');

    cy.wait('@session')
      .get('.mat-card')
      .last()
      .contains('button', 'Detail')
      .click();
    cy.wait('@sessionBeforeUpdate')
      .get('.mat-card')
      .last()
      .contains('button', 'Delete')
      .click();
    cy.log('**The session is deleted**');
    cy.wait('@sessionDeleted').url().should('include', '/sessions');
  });
});
