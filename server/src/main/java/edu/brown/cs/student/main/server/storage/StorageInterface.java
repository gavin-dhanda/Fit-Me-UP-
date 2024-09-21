package edu.brown.cs.student.main.server.storage;

import com.google.cloud.firestore.DocumentReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** An interface that declares document utility for a user. */
public interface StorageInterface {

  void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

  List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException;

  void clearUser(String uid) throws InterruptedException, ExecutionException;

  void deleteDocument(DocumentReference doc);

  Map<String, Object> getDocument(DocumentReference doc)
      throws InterruptedException, ExecutionException;

  DocumentReference getDocumentReference(String uid, String collection_id, String doc_id);
}
