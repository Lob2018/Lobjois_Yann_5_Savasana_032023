export const login = (role: string, email: string, password: string) => {
  // Returns the fixture of the corresponding user
  cy.fixture(role).then((json) => {
    cy.intercept('POST', '/api/auth/login', {
      body: json,
    });
  });
  cy.visit('/login');
  cy.get('input[formControlName=email]').type(email);
  cy.get('input[formControlName=password]').type(`${password}{enter}{enter}`);
};
