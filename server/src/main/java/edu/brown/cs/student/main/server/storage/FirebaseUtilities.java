package edu.brown.cs.student.main.server.storage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/** A class that handles the firebase functionality. */
public class FirebaseUtilities implements StorageInterface {

  /**
   * Constructor for the FirebaseUtilities class.
   *
   * @throws IOException if the file is not found.
   */
  public FirebaseUtilities() throws IOException {
    String workingDirectory = System.getProperty("user.dir");
    Path firebaseConfigPath =
        Paths.get(workingDirectory, "src", "main", "resources", "firebase_config.json");

    FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath.toString());

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    if (FirebaseApp.getApps().isEmpty()) { // Check if the default app has not been initialized
      FirebaseApp.initializeApp(options); // Initialize with options
    } else {
      FirebaseApp.getInstance(); // Get the already initialized instance
    }
  }

  /** Function that adds a new document to a collection for a specified user. */
  @Override
  public void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data)
      throws IllegalArgumentException {
    if (uid == null || collection_id == null || doc_id == null || data == null) {
      throw new IllegalArgumentException(
          "addDocument: uid, collection_id, doc_id, or data cannot be null");
    }

    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collection_id);
    collectionRef.document(doc_id).set(data);
  }

  /** Gets all the documents in a collection for a specified user. */
  @Override
  public List<Map<String, Object>> getCollection(String uid, String collection_id)
      throws InterruptedException, ExecutionException, IllegalArgumentException {
    if (uid == null || collection_id == null) {
      throw new IllegalArgumentException("getCollection: uid and/or collection_id cannot be null");
    }

    Firestore db = FirestoreClient.getFirestore();
    CollectionReference dataRef = db.collection("users").document(uid).collection(collection_id);
    QuerySnapshot dataQuery = dataRef.get().get();
    List<Map<String, Object>> data = new ArrayList<>();
    for (QueryDocumentSnapshot doc : dataQuery.getDocuments()) {
      data.add(doc.getData());
    }

    return data;
  }

  /** Gets a document reference for a specified user. */
  @Override
  public DocumentReference getDocumentReference(
      String uid, String collection_id, String document_id) {
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference collectionRef =
        db.collection("users").document(uid).collection(collection_id);
    return collectionRef.document(document_id);
  }

  /** Clears the data for a specified user. */
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    try {
      Firestore db = FirestoreClient.getFirestore();
      DocumentReference userDoc = db.collection("users").document(uid);
      deleteDocument(userDoc);
    } catch (Exception e) {
      System.err.println("Error removing user : " + uid);
      System.err.println(e.getMessage());
    }
  }

  /**
   * Deletes a document within a user's collection.
   *
   * @param doc the document to delete,
   */
  @Override
  public void deleteDocument(DocumentReference doc) {
    Iterable<CollectionReference> collections = doc.listCollections();
    for (CollectionReference collection : collections) {
      deleteCollection(collection);
    }
    doc.delete();
  }

  /**
   * Gets the data from a document.
   *
   * @param doc the document to get data from.
   * @return the data from the document.
   */
  @Override
  public Map<String, Object> getDocument(DocumentReference doc)
      throws InterruptedException, ExecutionException {
    return doc.get().get().getData();
  }

  /**
   * Deletes all the documents in a user's collection.
   *
   * @param collection the collection to delete.
   */
  private void deleteCollection(CollectionReference collection) {
    try {

      // Get all documents in the collection
      ApiFuture<QuerySnapshot> future = collection.get();
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();

      // Delete each document
      for (QueryDocumentSnapshot doc : documents) {
        doc.getReference().delete();
      }

    } catch (Exception e) {
      System.err.println("Error deleting collection : " + e.getMessage());
    }
  }
}
