describe('404 spec', () => {
  it('404 successfull', () => {
    cy.request({ url: '/unknown', failOnStatusCode: false })
      .its('status')
      .should('equal', 404);
    cy.visit('/unknown', { failOnStatusCode: false });
    cy.get('h1').contains('Page not found !');
  });
});
