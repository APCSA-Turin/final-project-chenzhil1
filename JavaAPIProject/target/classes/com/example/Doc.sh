# 1. Start headless X server on display :99
Xvfb :99 -screen 0 1665x915x24 -nolisten tcp -nolisten unix &

# 2. Tell apps to use display :99
export DISPLAY=:99

# 3. Launch the window manager
fluxbox &

# 4. Serve the display via VNC, no password, persistent
x11vnc -display :99 -nopw -forever -shared &


websockify --web=/usr/share/novnc 6080 localhost:5900 &

cd /workspaces/final-project-chenzhil1

# 1. Start headless X server on display :99
Xvfb :99 -screen 0 1665x915x24 -nolisten tcp -nolisten unix &

# 2. Tell apps to use display :99
export DISPLAY=:99

# 3. Launch the window manager
fluxbox &

# 4. Serve the display via VNC, no password, persistent
x11vnc -display :99 -nopw -forever -shared &

