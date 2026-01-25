import request from '../request'

export interface PermissionResponse {
  id: string
  name: string
  code: string
  description: string
  category: string
}

export interface RoleResponse {
  id: string
  name: string
  description: string
  permissions: PermissionResponse[]
  createdAt: number
  updatedAt: number
}

export interface RoleRequest {
  name: string
  description?: string
  permissionIds?: string[]
}

const roleService = {
  // Get all roles
  getAllRoles: async (): Promise<RoleResponse[]> => {
    const response = await request.get('/api/admin/roles')
    return response.data
  },

  // Get role by ID
  getRoleById: async (id: string): Promise<RoleResponse> => {
    const response = await request.get(`/api/admin/roles/${id}`)
    return response.data
  },

  // Create new role
  createRole: async (data: RoleRequest): Promise<RoleResponse> => {
    const response = await request.post('/api/admin/roles', data)
    return response.data
  },

  // Update role
  updateRole: async (id: string, data: RoleRequest): Promise<RoleResponse> => {
    const response = await request.put(`/api/admin/roles/${id}`, data)
    return response.data
  },

  // Delete role
  deleteRole: async (id: string): Promise<void> => {
    await request.delete(`/api/admin/roles/${id}`)
  },

  // Assign permissions to role
  assignPermissions: async (roleId: string, permissionIds: string[]): Promise<RoleResponse> => {
    const response = await request.put(`/api/admin/roles/${roleId}/permissions`, permissionIds)
    return response.data
  },

  // Get all permissions
  getAllPermissions: async (): Promise<PermissionResponse[]> => {
    const response = await request.get('/api/admin/roles/permissions')
    return response.data
  },
}

export default roleService
