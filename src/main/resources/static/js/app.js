// ===== CONFIGURACIÓN INICIAL =====

// Constantes con las URLs de la API para comunicarse con el backend
const API_URL = '/api';
const SERVICIO_URL = `${API_URL}/servicio`;
const FACTURA_URL = `${API_URL}/factura`;

// Referencias a elementos HTML del DOM para manipularlos fácilmente
// Esto nos permite acceder a los elementos sin usar document.getElementById cada vez
const servicioForm = document.getElementById('servicioForm');
const nombreServicioInput = document.getElementById('nombreServicio');
const costoServicioInput = document.getElementById('costoServicio');
const tablaServicios = document.getElementById('tablaServicios').querySelector('tbody');
const tablaFacturas = document.getElementById('tablaFacturas').querySelector('tbody');
const listaServiciosFactura = document.getElementById('listaServiciosFactura');
const mensajeNoServicios = document.getElementById('mensajeNoServicios');
const facturaForm = document.getElementById('facturaForm');
const facturaIdConsulta = document.getElementById('facturaIdConsulta');
const btnConsultarFactura = document.getElementById('btnConsultarFactura');
const detalleFacturaBody = document.getElementById('detalleFacturaBody');
const detalleFacturaModal = new bootstrap.Modal(document.getElementById('detalleFacturaModal'));

// Variables globales para almacenar datos
let servicios = []; // Array con todos los servicios disponibles
let facturas = [];  // Array con todas las facturas generadas

// ===== FUNCIONES PARA GESTIONAR SERVICIOS =====

/**
 * Obtiene todos los servicios desde el servidor y los almacena.
 * Esta función se ejecuta al cargar la página para mostrar los servicios existentes.
 */
async function cargarServicios() {
    try {
        // Hacer petición GET al endpoint de servicios
        const response = await fetch(SERVICIO_URL);
        if (!response.ok) {
            throw new Error('Error al cargar servicios');
        }
        
        // Guardar los servicios obtenidos y actualizar la interfaz
        servicios = await response.json();
        renderizarServicios();
        renderizarServiciosEnFactura();
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta('Error al cargar servicios', 'error');
    }
}

/**
 * Actualiza la tabla de servicios en la interfaz con los datos actuales.
 * Muestra cada servicio con su ID, nombre, costo y botón para ver detalles.
 */
function renderizarServicios() {
    // Limpiar la tabla antes de volver a llenarla
    tablaServicios.innerHTML = '';
    
    // Si no hay servicios, mostrar un mensaje
    if (servicios.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="4" class="text-center">No hay servicios registrados</td>`;
        tablaServicios.appendChild(row);
        return;
    }
    
    // Para cada servicio, crear una fila en la tabla
    servicios.forEach(servicio => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${servicio.id}</td>
            <td>${servicio.nombre}</td>
            <td>$${servicio.costo.toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-info" onclick="verDetalleServicio('${servicio.id}')">
                    Ver
                </button>
            </td>
        `;
        tablaServicios.appendChild(row);
    });
}

/**
 * Actualiza la lista de servicios en el formulario de creación de facturas.
 * Crea un checkbox por cada servicio disponible para que el usuario pueda seleccionarlos.
 */
function renderizarServiciosEnFactura() {
    // Limpiar la lista de servicios
    listaServiciosFactura.innerHTML = '';
    
    // Si no hay servicios, mostrar mensaje de "No hay servicios disponibles"
    if (servicios.length === 0) {
        listaServiciosFactura.appendChild(mensajeNoServicios);
        return;
    }
    
    // Para cada servicio, crear un checkbox en la lista
    servicios.forEach(servicio => {
        const servicioItem = document.createElement('div');
        servicioItem.className = 'form-check mb-2';
        servicioItem.innerHTML = `
            <input class="form-check-input servicio-checkbox" type="checkbox" value="${servicio.id}" id="servicio_${servicio.id}">
            <label class="form-check-label" for="servicio_${servicio.id}">
                ${servicio.nombre} - $${servicio.costo.toFixed(2)}
            </label>
        `;
        listaServiciosFactura.appendChild(servicioItem);
    });
}

/**
 * Maneja el envío del formulario para agregar un nuevo servicio.
 * Valida los datos, envía la petición al servidor y actualiza la interfaz.
 * 
 * @param {Event} e - Evento del formulario
 */
async function agregarServicio(e) {
    // Prevenir el comportamiento predeterminado del formulario (recargar la página)
    e.preventDefault();
    
    // Obtener y validar los valores del formulario
    const nombre = nombreServicioInput.value.trim();
    const costo = parseFloat(costoServicioInput.value);
    
    // Validar que los datos sean correctos
    if (!nombre || costo <= 0) {
        mostrarAlerta('El nombre es requerido y el costo debe ser mayor que cero', 'warning');
        return;
    }
    
    // Crear objeto con los datos del servicio
    const servicio = {
        nombre: nombre,
        costo: costo
    };
    
    try {
        // Enviar solicitud POST para crear el servicio
        const response = await fetch(SERVICIO_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(servicio)
        });
        
        // Verificar si la solicitud fue exitosa
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Error al registrar el servicio');
        }
        
        // Obtener el servicio creado y agregarlo a la lista
        const nuevoServicio = await response.json();
        servicios.push(nuevoServicio);
        
        // Actualizar la interfaz
        renderizarServicios();
        renderizarServiciosEnFactura();
        
        // Limpiar el formulario
        nombreServicioInput.value = '';
        costoServicioInput.value = '';
        
        // Mostrar mensaje de éxito
        mostrarAlerta('Servicio registrado con éxito', 'success');
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

/**
 * Muestra los detalles de un servicio específico en una ventana emergente.
 * 
 * @param {string} id - ID del servicio a mostrar
 */
async function verDetalleServicio(id) {
    try {
        // Obtener los detalles del servicio
        const response = await fetch(`${SERVICIO_URL}/${id}`);
        
        if (!response.ok) {
            throw new Error('No se pudo obtener el detalle del servicio');
        }
        
        // Obtener los datos del servicio
        const servicio = await response.json();
        
        // Mostrar un popup con los detalles usando SweetAlert
        Swal.fire({
            title: 'Detalle del Servicio',
            html: `
                <div class="text-start">
                    <p><strong>ID:</strong> ${servicio.id}</p>
                    <p><strong>Nombre:</strong> ${servicio.nombre}</p>
                    <p><strong>Costo:</strong> $${servicio.costo.toFixed(2)}</p>
                </div>
            `,
            icon: 'info'
        });
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

// ===== FUNCIONES PARA GESTIONAR FACTURAS =====

/**
 * Obtiene todas las facturas desde el servidor y las almacena.
 * Esta función se ejecuta al cargar la página para mostrar las facturas existentes.
 */
async function cargarFacturas() {
    try {
        // Hacer petición GET al endpoint de facturas
        const response = await fetch(FACTURA_URL);
        
        if (!response.ok) {
            throw new Error('Error al cargar facturas');
        }
        
        // Guardar las facturas obtenidas y actualizar la interfaz
        facturas = await response.json();
        renderizarFacturas();
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta('Error al cargar facturas', 'error');
    }
}

/**
 * Actualiza la tabla de facturas en la interfaz con los datos actuales.
 * Muestra cada factura con su ID, total, estado y botones de acción.
 */
function renderizarFacturas() {
    // Limpiar la tabla antes de volver a llenarla
    tablaFacturas.innerHTML = '';
    
    // Si no hay facturas, mostrar un mensaje
    if (facturas.length === 0) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="4" class="text-center">No hay facturas generadas</td>`;
        tablaFacturas.appendChild(row);
        return;
    }
    
    // Para cada factura, crear una fila en la tabla
    facturas.forEach(factura => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${factura.id}</td>
            <td>$${factura.total.toFixed(2)}</td>
            <td>
                <span class="badge ${factura.pagada ? 'bg-success' : 'bg-warning'}">
                    ${factura.pagada ? 'Pagada' : 'Pendiente'}
                </span>
            </td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="verDetalleFactura('${factura.id}')">
                    Ver
                </button>
                ${!factura.pagada ? `<button class="btn btn-sm btn-success" onclick="pagarFactura('${factura.id}')">Pagar</button>` : ''}
            </td>
        `;
        tablaFacturas.appendChild(row);
    });
}

/**
 * Maneja el envío del formulario para crear una nueva factura.
 * Recoge los servicios seleccionados, envía la petición al servidor y actualiza la interfaz.
 * 
 * @param {Event} e - Evento del formulario
 */
async function crearFactura(e) {
    // Prevenir el comportamiento predeterminado del formulario
    e.preventDefault();
    
    // Obtener los checkboxes seleccionados (servicios para la factura)
    const checkboxes = document.querySelectorAll('.servicio-checkbox:checked');
    const serviciosIds = Array.from(checkboxes).map(cb => cb.value);
    
    // Validar que se haya seleccionado al menos un servicio
    if (serviciosIds.length === 0) {
        mostrarAlerta('Debe seleccionar al menos un servicio', 'warning');
        return;
    }
    
    // Crear objeto con los datos para la factura
    const facturaRequest = {
        serviciosIds: serviciosIds
    };
    
    try {
        // Enviar solicitud POST para crear la factura
        const response = await fetch(FACTURA_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(facturaRequest)
        });
        
        // Verificar si la solicitud fue exitosa
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Error al generar la factura');
        }
        
        // Obtener la factura creada y agregarla a la lista
        const nuevaFactura = await response.json();
        facturas.push(nuevaFactura);
        
        // Actualizar la interfaz
        renderizarFacturas();
        
        // Desmarcar todos los checkboxes
        checkboxes.forEach(cb => cb.checked = false);
        
        // Mostrar mensaje de éxito
        mostrarAlerta(`Factura generada con éxito. ID: ${nuevaFactura.id}`, 'success');
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

/**
 * Maneja la acción de consultar una factura por su ID.
 * Obtiene el ID desde el input y llama a la función de mostrar detalles.
 */
async function consultarFactura() {
    // Obtener el ID de la factura del campo de texto
    const id = facturaIdConsulta.value.trim();
    
    // Validar que se haya ingresado un ID
    if (!id) {
        mostrarAlerta('Ingrese un ID de factura', 'warning');
        return;
    }
    
    try {
        // Mostrar los detalles de la factura
        await verDetalleFactura(id);
        // Limpiar el campo después de consultar
        facturaIdConsulta.value = '';
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

/**
 * Muestra los detalles de una factura específica en un modal.
 * 
 * @param {string} id - ID de la factura a mostrar
 */
async function verDetalleFactura(id) {
    try {
        // Obtener los detalles de la factura
        const response = await fetch(`${FACTURA_URL}/${id}`);
        
        if (!response.ok) {
            throw new Error('No se pudo obtener el detalle de la factura');
        }
        
        // Obtener los datos de la factura
        const factura = await response.json();
        
        // Crear el HTML para la tabla de servicios incluidos
        const serviciosHTML = factura.servicios.map(servicio => `
            <tr>
                <td>${servicio.nombre}</td>
                <td class="text-end">$${servicio.costo.toFixed(2)}</td>
            </tr>
        `).join('');
        
        // Preparar el contenido del modal con la información de la factura
        detalleFacturaBody.innerHTML = `
            <div class="border p-3 mb-3">
                <h6>Información de la Factura</h6>
                <p><strong>ID:</strong> ${factura.id}</p>
                <p><strong>Estado:</strong> 
                    <span class="badge ${factura.pagada ? 'bg-success' : 'bg-warning'}">
                        ${factura.pagada ? 'Pagada' : 'Pendiente'}
                    </span>
                </p>
            </div>
            
            <div class="border p-3">
                <h6>Servicios Incluidos</h6>
                <table class="table table-sm">
                    <thead>
                        <tr>
                            <th>Servicio</th>
                            <th class="text-end">Costo</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${serviciosHTML}
                    </tbody>
                    <tfoot>
                        <tr>
                            <th>Total</th>
                            <th class="text-end">$${factura.total.toFixed(2)}</th>
                        </tr>
                    </tfoot>
                </table>
            </div>
            
            ${!factura.pagada ? `
                <div class="mt-3 text-end">
                    <button class="btn btn-success" onclick="pagarFactura('${factura.id}')">
                        Pagar Factura
                    </button>
                </div>
            ` : ''}
        `;
        
        // Mostrar el modal con los detalles
        detalleFacturaModal.show();
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

/**
 * Marca una factura como pagada.
 * 
 * @param {string} id - ID de la factura a pagar
 */
async function pagarFactura(id) {
    try {
        // Enviar solicitud PUT para pagar la factura
        const response = await fetch(`${FACTURA_URL}/${id}/pagar`, {
            method: 'PUT'
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Error al pagar la factura');
        }
        
        // Obtener la factura actualizada
        const facturaActualizada = await response.json();
        
        // Actualizar la factura en la lista local
        const index = facturas.findIndex(f => f.id === id);
        if (index !== -1) {
            facturas[index] = facturaActualizada;
        }
        
        // Actualizar la interfaz
        renderizarFacturas();
        
        // Si el modal está abierto, actualizar su contenido
        if (detalleFacturaModal._isShown) {
            await verDetalleFactura(id);
        }
        
        // Mostrar mensaje de éxito
        mostrarAlerta('Factura pagada con éxito', 'success');
    } catch (error) {
        console.error('Error:', error);
        mostrarAlerta(error.message, 'error');
    }
}

// ===== FUNCIONES AUXILIARES =====

/**
 * Muestra una alerta visual al usuario usando SweetAlert.
 * 
 * @param {string} mensaje - Texto a mostrar
 * @param {string} tipo - Tipo de alerta ('success', 'error', 'warning', 'info')
 */
function mostrarAlerta(mensaje, tipo) {
    Swal.fire({
        title: tipo === 'error' ? 'Error' : tipo === 'success' ? 'Éxito' : 'Atención',
        text: mensaje,
        icon: tipo,
        confirmButtonText: 'Aceptar'
    });
}

// ===== INICIALIZACIÓN =====

/**
 * Cuando el DOM está completamente cargado, inicializa la aplicación:
 * - Carga los servicios y facturas existentes
 * - Configura los eventos para los formularios y botones
 */
document.addEventListener('DOMContentLoaded', () => {
    cargarServicios();
    cargarFacturas();
    
    servicioForm.addEventListener('submit', agregarServicio);
    facturaForm.addEventListener('submit', crearFactura);
    btnConsultarFactura.addEventListener('click', consultarFactura);
}); 