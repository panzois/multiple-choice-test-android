# PROJECT CONTRACT – Android Multiple Choice Test

This document defines the FIXED parts of the project.
These must NOT be changed without agreement.

---

## 1. Activity Flow (FIXED)
SplashActivity  
→ LoginActivity  
→ TestActivity  
→ ResultActivity

---

## 2. Intent Extras (FIXED KEYS)

From LoginActivity → TestActivity:
- EXTRA_USERNAME (String)

From TestActivity → ResultActivity:
- EXTRA_SCORE (int)
- EXTRA_TOTAL (int)
- EXTRA_USERNAME (String)
- EXTRA_TIMESTAMP (long)

---

## 3. Layout IDs (DO NOT CHANGE)

### activity_login.xml
- EtName
- BtBegin

### activity_test.xml
- TvTimer
- TvQuestion
- IvQuestion
- BtChoice1
- BtChoice2
- BtChoice3
- BtChoice4
- BtChoice5
- BtNext

### activity_result.xml
- TvScore
- BtRestart

Root layout id (ALL layouts):
- main

---

## 4. Responsibilities Summary

Role A – Integrator:
- Navigation
- Activity flow
- Integration
- Final testing

Role B – Quiz Engine:
- Question logic
- Random selection
- Scoring
- NO UI

Role C – UI / Timer / Results:
- Layouts
- Timer
- Result display
- NO quiz logic

---

## 5. Files that ONLY Integrator can modify
- AndroidManifest.xml
- Activity flow
- Intent extras