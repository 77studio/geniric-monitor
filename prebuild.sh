#!/bin/bash
echo 'echo Hello, out.' > /tmp/generic-monitor-test.sh
echo 'echo Hello, err. >&2' >> /tmp/generic-monitor-test.sh
chmod +x /tmp/generic-monitor-test.sh
