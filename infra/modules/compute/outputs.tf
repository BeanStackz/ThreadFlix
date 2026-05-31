output "vm_id" {
  value = azurerm_linux_virtual_machine.ffmpeg_vm.id
}

output "principal_id" {
  value = azurerm_linux_virtual_machine.ffmpeg_vm.identity[0].principal_id
}

output "public_ip_address" {
  value = azurerm_public_ip.ffmpeg_public_ip.ip_address
}