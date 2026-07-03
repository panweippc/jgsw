import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref<string[]>([])

  function setPermissions(perms: string[]) {
    permissions.value = perms
  }

  function hasPermission(perm: string): boolean {
    return permissions.value.includes(perm)
  }

  function clearPermissions() {
    permissions.value = []
  }

  return { permissions, setPermissions, hasPermission, clearPermissions }
})
