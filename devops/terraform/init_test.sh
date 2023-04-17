#!/bin/sh
cd terratest
go mod init "test/terratest"
go mod tidy

go test -v -timeout 60m
