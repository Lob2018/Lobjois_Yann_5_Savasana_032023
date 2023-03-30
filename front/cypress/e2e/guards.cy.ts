describe('UnauthGuard', () => {
  it('Should redirect to login when the user is not logged in', () => {
    cy.visit('/sessions');
    cy.url().url().should('include', '/login');
  });
});
