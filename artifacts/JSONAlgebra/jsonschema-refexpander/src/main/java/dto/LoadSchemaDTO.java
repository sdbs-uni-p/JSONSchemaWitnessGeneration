package dto;

import model.normalization.RepositoryType;

/**
 * DTO to store information about how schemas should be loaded. It is stored whether distributed
 * schemas should be allowed, whether references of the schema should be loaded online and from
 * which repository the schema is from.
 * 
 * @author Lukas Ellinger
 */
public class LoadSchemaDTO {
  private final boolean allowDistributedSchemas;
  private final boolean fetchSchemasOnline;
  private RepositoryType repType;

  public boolean isAllowDistributedSchemas() {
    return allowDistributedSchemas;
  }

  public boolean isFetchSchemasOnline() {
    return fetchSchemasOnline;
  }

  public RepositoryType getRepType() {
    return repType;
  }

  public void setRepType(RepositoryType repType) {
    this.repType = repType;
  }

  public static LoadSchemaDTO of(boolean allowDistributedSchemas, boolean fetchSchemasOnline,
      RepositoryType repType) {
    return new LoadSchemaDTO(allowDistributedSchemas, fetchSchemasOnline, repType);
  }

  private LoadSchemaDTO(boolean allowDistributedSchemas, boolean fetchSchemasOnline,
      RepositoryType repType) {
    this.allowDistributedSchemas = allowDistributedSchemas;
    this.fetchSchemasOnline = fetchSchemasOnline;
    this.repType = repType;
  }

  public static class Builder {
    private boolean allowDistributedSchemas;
    private boolean fetchSchemasOnline;
    private RepositoryType repType;

    public Builder allowDistributedSchemas(boolean allowDistributedSchemas) {
      this.allowDistributedSchemas = allowDistributedSchemas;
      return this;
    }

    public Builder fetchSchemasOnline(boolean fetchSchemasOnline) {
      this.fetchSchemasOnline = fetchSchemasOnline;
      return this;
    }

    public Builder setRepType(RepositoryType repType) {
      this.repType = repType;
      return this;
    }

    public LoadSchemaDTO build() {
      return LoadSchemaDTO.of(allowDistributedSchemas, fetchSchemasOnline, repType);
    }
  }
}
