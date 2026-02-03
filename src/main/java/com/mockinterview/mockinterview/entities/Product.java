package com.mockinterview.mockinterview.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Product")
public class Product {
  @Id
  private String id;
  private String uuid;
  private boolean enabled;
  private String family;
  private List<String> categories;
  private List<String> groups;
  private String parent;
  private String root_parent;

  // 🔥 Key part: dynamic attributes
  private Map<String, List<AttributesValue>> values;

  private LocalDateTime created;
  private LocalDateTime updated;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getFamily() {
    return family;
  }

  public void setFamily(String family) {
    this.family = family;
  }

  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public List<String> getGroups() {
    return groups;
  }

  public void setGroups(List<String> groups) {
    this.groups = groups;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getRoot_parent() {
    return root_parent;
  }

  public void setRoot_parent(String root_parent) {
    this.root_parent = root_parent;
  }

  public Map<String, List<AttributesValue>> getValues() {
    return values;
  }

  public void setValues(
      Map<String, List<AttributesValue>> values) {
    this.values = values;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }
}
