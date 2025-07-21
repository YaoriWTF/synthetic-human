# Новое окно терминала для мониторинга
watch -n 1 'curl -s http://localhost:8081/actuator/metrics | grep weyland'