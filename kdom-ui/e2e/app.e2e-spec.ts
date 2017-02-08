import { KdomUiPage } from './app.po';

describe('kdom-ui App', function() {
  let page: KdomUiPage;

  beforeEach(() => {
    page = new KdomUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
