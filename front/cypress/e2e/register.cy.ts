describe('Register spec', () => {
  it('Register successfull', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      body: {
        message: 'User registered successfully!',
      },
    }).as('register');

    cy.get('input[formControlName=firstName]').type('yoga');
    cy.get('input[formControlName=lastName]').type('yoga');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.wait('@register').url().should('include', '/login');
  });
});
