import { EmbedModule } from './embed.module';

describe('EmbedModule', () => {
  let embedModule: EmbedModule;

  beforeEach(() => {
    embedModule = new EmbedModule();
  });

  it('should create an instance', () => {
    expect(embedModule).toBeTruthy();
  });
});
