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

    usersSnapshot.forEach(async (doc) => {
      const user = doc.data();
      const currentLives = user.vidas;
      const updatedLifeValue = currentLives + 1;

      // Verifica si el usuario ha alcanzado las 20 vidas por primera vez
      if (currentLives < 20 && updatedLifeValue >= 20) {
        // Envía una notificación push al dispositivo del usuario
        const message = {
          notification: {
            title: "Recarga de vidas",
            body: "¡Felicidades! Tus vidas se han recargado a 20.",
          },
          token: user.token, // Token de registro del dispositivo del usuario
        };

        await admin.messaging().send(message);
      }

      // Actualiza las vidas del usuario solo si es menor a 20
      if (currentLives < 20) {
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
