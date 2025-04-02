# Morsey

## Table of Contents
- [Overview](#overview)
- [Required Robot Parts](#required-robot-parts)
- [Decomposition view](#decomposition-view)
- [UML Class Diagram](#uml-class-diagram)

## Overview
The Morse Code Reader Robot is an autonomous robot that reads Morse code from paper, interprets it, and performs specific instructions. It can also accept Morse code input via a touch sensor and navigate while avoiding obstacles. The robot is built using the **LEGO Mindstorms EV3** and programmed with the **Java leJOS API**.

## Required Robot Parts
- **LEGO Mindstorms EV3 Brick** - Central processing unit.
- **Colour Sensor** - Reads the Morse code lines.
- **Touch Sensor** - Allows manual Morse code input.
- **Ultrasonic Sensor** - Detects obstacles and avoids them.
- **Two Large Motors** - Controls robot movement.

## Packages
main - contains main files to run the robot. 'Morsey' is the main java driver class that should be compiled.
behaviors - contains the robots behaviors.
hardware - contains classes dealing with the sensors and motors of the robot.
logging - package containing the Logger.
morse - package containing classes dealing with touch and colour morse input.

## Features
- Reads Morse code from paper.
- Avoids obstacles using an ultrasonic sensor and waypoints.
- Understands instructions embedded in Morse code (e.g., "TURN LEFT").
- Allows user input via the touch sensor.
- Displays translated Morse code on screen.
- Plays sound when Morse code is read.

## Decomposition view
![Untitled (Draft)-1](https://github.com/user-attachments/assets/05b48a41-f6c4-4b3b-af3e-1cdc262501c6)
![Untitled (Draft)-2](https://github.com/user-attachments/assets/887a2ad0-7033-4ee1-bbd2-348957e44307)

## UML Class Diagram
![UML class](https://github.com/user-attachments/assets/3bc1e0b2-7a67-48b2-922b-483bdc0445f9)

