#!/bin/bash
kill -9 `ps -ef  | grep Server | grep java | grep -v grep | awk '{print $2}'`
kill -9 `ps -ef  | grep rmiregistry| grep -v grep | awk '{print $2}'`