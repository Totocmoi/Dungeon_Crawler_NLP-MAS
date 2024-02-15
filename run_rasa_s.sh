#!/bin/bash
cd Rasa/Assistant/
echo "[RASA] Loading source"
source "C:\Users\totol\venv\Scripts\activate"
if ! [ "$(ls ./models/)" ]; then
	echo "[RASA] Training model"
	rasa train
fi
echo "[RASA] Starting rasa shell"
winpty rasa shell