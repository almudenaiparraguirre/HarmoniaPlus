const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

const db = admin.firestore();

exports.updateLifeValues =
functions.pubsub.schedule("every 5 minutes").onRun(async (context) => {
  try {
    const usersRef = db.collection("usuarios");
    const usersSnapshot = await usersRef.get();

    const batch = db.batch();

    usersSnapshot.forEach((doc) => {
      const user = doc.data();
      // Comprueba si el valor de vida es menor a 20 antes de incrementarlo
      if (user.vidas < 20) {
        const updatedLifeValue =
        user.vidas + 1; // Incrementa el valor de vida en 1
        batch.update(doc.ref, {vidas: updatedLifeValue});
      }
    });

    await batch.commit();

    functions.logger.info("Life values updated successfully");
    return null;
  } catch (error) {
    functions.logger.error("Error updating life values:", error);
    return null;
  }
});
