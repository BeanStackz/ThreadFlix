output "vm_id" {
  value       = module.compute.vm_id
  description = "The generated system Resource ID for your deployed compute node."
}

output "vm_principal_id" {
  value       = module.compute.principal_id
  description = "The Principal ID tied to the system-assigned managed identity used for native storage authentication."
}

output "vm_public_ip" {
  value       = module.compute.public_ip_address
  description = "The public IP address assigned to your processing node."
}